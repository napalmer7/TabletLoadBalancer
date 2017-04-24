package tabletbalance;

import java.util.*;
import java.util.logging.*;

public class MasterController extends Master {
  private static final Logger LOGGER = Logger.getLogger( MasterController.class.getName() );
  private Tablet[] tablets;
  private long keyRange;

  public MasterController (int numTablets, List<String> serverNames){
    super(numTablets, serverNames);

    this.keyRange = Long.MAX_VALUE / numTablets;

    // Create a new array to hold the tablets
    this.tablets = new Tablet[numTablets];

    for ( int i =0; i < numTablets; i++)
    {
      if ((i+1) >= numTablets){
        // The last one will always go to the end
        this.tablets[i] = new Tablet(i*keyRange, Long.MAX_VALUE );
      }
      else{
        this.tablets[i] = new Tablet(i*keyRange, ((i+1)*keyRange) - 1 );
      }
      LOGGER.log( Level.INFO, "Adding tablet index {0} with range {1} to {2}.", new Object[] {i, this.tablets[i].getMinimum(), this.tablets[i].getMaxmimum()});
    }

    this.rebuildTabletMapping();
  }

  public String getServerForKey(long key)
  {
    if (key < 0)
    {
      LOGGER.log( Level.SEVERE, "getServerForKey: An invalid key {0} was specified.", key);
      throw new IllegalArgumentException("Invalid key specified: key must be greater than or equal to 0.");
    }
    // Use the stored keyrange to act as a quick lookup/hash type of key evaluator
    int index = (int)(key / this.keyRange);

    if(index >= numTablets){
      // Anything equal to the number of tablets is at max index
      index = this.tablets.length - 1;
    }

    return this.tablets[index].getHostingServer();
  }

  private void rebuildTabletMapping()
  {
    if (this.serverNames.size() > this.numTablets){
      // This should generate a warning message since there's idle servers.
      LOGGER.log( Level.WARNING, "rebuildTabletMapping: The number of servers exceeds the number of available tablets.");
    }

    int i =0;
    for(Tablet t : this.tablets){
      t.setHostingServer(this.serverNames.get(i));
      i++;
      i = i % this.serverNames.size();  // Use modulo for scenarios where len tabs > servers
    }
  }

  public void addServer(String serverName){
    if (serverName == null || serverName.trim().isEmpty())
    {
      LOGGER.log( Level.SEVERE, "getServerForKey: An invalid server name '{0}' was specified.", serverName);
      throw new IllegalArgumentException("Invalid server name specified: serverName must have at least 1 non-whitespace character.");
    }
    else if (this.serverNames.contains(serverName)){
      // No work, it's already there
      return;
    }
    else
    {
      this.serverNames.add(serverName);
      // Rebuild!
      rebuildTabletMapping();
    }
  }

  public void removeServer(String serverName){
    if (serverName == null || serverName.trim().isEmpty())
    {
      throw new IllegalArgumentException("Invalid server name specified: serverName must have at least 1 non-whitespace character.");
    }
    else if (!this.serverNames.contains(serverName)){
      // No work, it's not there... this could also be an error case
      return;
    }
    else
    {
      this.serverNames.remove(this.serverNames.indexOf(serverName));
      // Rebuild!
      rebuildTabletMapping();
    }
  }

  public int getServerCount()
  {
    return this.serverNames.size();
  }
}
