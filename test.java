import tabletbalance.*;
import java.util.*;

public class test {
    public static void main(String args[]) {
        System.out.println("Testing master controller logic...");
        List<String> servers = new ArrayList<String>();
        servers.add("Test");
        servers.add("Servers");
        servers.add("Winning");
        servers.add("OhNo");

        System.out.println("Initializing MC.");
        MasterController mc = new MasterController(5, servers);

        basic_test(mc);
        basic_negative(mc);
        add_extras_test(mc);
        add_duplicate_test(mc);
        remove_duplicate_test(mc);
    }

    public static void lookup_key(MasterController mc, long key)
    {
      System.out.println(String.format("Looking for key %d...", key));
      try{
        String server = mc.getServerForKey(key);
        System.out.println(String.format("Found %s at %d", server, key));
      }catch (Exception e)
      {
        System.out.println(String.format("Exception occurred looking up key %d. %s", key, e.getMessage()));
        throw e;
      }
    }

    public static void lookup_set(MasterController mc, int count)
    {
      for (int i = 0; i < count; i++)
      {
        if (i == 0)
        {
          lookup_key(mc, 0);
        }
        else{
          lookup_key(mc, Long.MAX_VALUE / i);
        }
      }
    }

    public static void basic_test(MasterController mc)
    {
      System.out.println();
      System.out.println("Starting basic test....");
      System.out.println("-----------------------");

      lookup_set(mc, 10);

      System.out.println();
      System.out.println("Adding new server Blarg");
      mc.addServer("Blarg");

      lookup_set(mc, 10);

      System.out.println();
      System.out.println("Removing server Servers");
      mc.removeServer("Servers");

      lookup_set(mc, 10);
    }

    public static void basic_negative(MasterController mc)
    {
      System.out.println();
      System.out.println("Starting basic negative test....");
      System.out.println("-----------------------");
      try
      {
        lookup_key(mc, -1);
      }
      catch (IllegalArgumentException e)
      {
        System.out.println("Properly handled invalid key state.");
      }
      catch (Exception e)
      {
        System.out.println(String.format("Invalid exception was raised... %s", e.getMessage()));
      }
    }

    public static void add_extras_test(MasterController mc)
    {
      System.out.println();
      System.out.println("Starting add extras test....");
      System.out.println("-----------------------");

      lookup_set(mc, 10);

      System.out.println();
      System.out.println("Adding new servers");
      mc.addServer("Ping");
      mc.addServer("Pong");
      mc.addServer("Silver");
      mc.addServer("Lining");

      lookup_set(mc, 10);

      System.out.println();
      System.out.println("Removing servers");
      mc.removeServer("Ping");
      mc.removeServer("Pong");
      mc.removeServer("Silver");
      mc.removeServer("Lining");
    }

    public static void add_duplicate_test(MasterController mc)
    {
      System.out.println();
      System.out.println("Starting add duplicates test....");
      System.out.println("-----------------------");

      lookup_set(mc, 10);

      System.out.println(String.format("Current server count %d", mc.getServerCount()));
      for (int i = 0; i < 2; i++)
      {
        System.out.println();
        System.out.println("Adding new servers");
        mc.addServer("Ping");
        mc.addServer("Pong");
        mc.addServer("Silver");
        mc.addServer("Lining");

        lookup_set(mc, 10);
      }

      System.out.println(String.format("After duplicate add server count %d", mc.getServerCount()));

      System.out.println();
      System.out.println("Removing servers");
      mc.removeServer("Ping");
      mc.removeServer("Pong");
      mc.removeServer("Silver");
      mc.removeServer("Lining");
    }

    public static void remove_duplicate_test(MasterController mc)
    {
      System.out.println();
      System.out.println("Starting remove duplicates test....");
      System.out.println("-----------------------");

      lookup_set(mc, 10);

      System.out.println(String.format("Current server count %d", mc.getServerCount()));
      System.out.println();
      System.out.println("Adding new servers");
      mc.addServer("Ping");
      mc.addServer("Pong");
      mc.addServer("Silver");
      mc.addServer("Lining");

      lookup_set(mc, 10);

      System.out.println(String.format("After add server count %d", mc.getServerCount()));

      System.out.println();
      System.out.println("Removing servers");
      mc.removeServer("Ping");
      mc.removeServer("Pong");
      mc.removeServer("Silver");
      mc.removeServer("Lining");

      System.out.println(String.format("After remove servers count %d", mc.getServerCount()));
      mc.removeServer("Ping");
      System.out.println(String.format("After remove duplicate servers count %d", mc.getServerCount()));
      mc.removeServer("Pong");
      System.out.println(String.format("After remove duplicate servers count %d", mc.getServerCount()));
    }
}
