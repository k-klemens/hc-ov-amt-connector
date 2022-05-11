package at.kk.msc.hcov.plugin.amt.util;

import java.net.URI;
import java.net.URISyntaxException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.mturk.MTurkClient;

public class MTurkClientCreator {

  public static MTurkClient getMTurkClient(boolean sandbox) {
    if(sandbox){
      return getSandboxClient();
    } else {
      return getClient();
    }
  }

  private static MTurkClient getSandboxClient() {
    try {
      return MTurkClient.builder()
          .region(Region.US_EAST_1)
          .endpointOverride(new URI("https://mturk-requester-sandbox.us-east-1.amazonaws.com"))
          .build();
    } catch (URISyntaxException e) {
      // ignore should not happen as URI is hardcoded
      return null;
    }
  }

  private static MTurkClient getClient() {
      return MTurkClient.builder()
          .region(Region.US_EAST_1)
          .build();
  }
}
