package tabletbalance;

public class Tablet{
  protected final long minimumKey;
  protected final long maximumKey;
  protected String hostingServer;

  public Tablet(long minKey, long maxKey)
  {
    this.minimumKey = minKey;
    this.maximumKey = maxKey;
  }

  public long getMinimum() {
    return this.minimumKey;
  }

  public long getMaxmimum() {
     return this.maximumKey;
  }

  public String getHostingServer(){
    return this.hostingServer;
  }

  public void setHostingServer(String server){
    this.hostingServer = server;
  }
}
