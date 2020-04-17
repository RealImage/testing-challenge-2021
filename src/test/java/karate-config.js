function() {
  var env = karate.env; // get system property 'karate.env' - Environments can be specified in runtime
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'staging';
  }
  var config = {
    env: env,
  }
  if (env == 'staging') {
    // For Unsafe https - Karate.configure('ssl',true) is required
    karate.configure('ssl', true);
    karate.configure('retry',{ count:10, interval:10000});
    config = karate.call('file:src/test/java/com/qube/api/tests/helpers/utility.js',config);
    config.envName = 'staging';
    config.baseUrl = 'https://ec2-35-154-146-139.ap-south-1.compute.amazonaws.com/sharebox/api/';
    karate.configure('connectTimeout', 60000);
    karate.configure('readTimeout', 60000);
    // Tokens of 4 different users
    config.primarytoken = 'a39c484f-7129-4fb1-ad57-68fd34a5fbec';
    config.firstrecipientToken = '995ed143-52fd-4a91-a0c4-b6349e251a57'
    config.secondrecipientToken = 'dfa3462-0746-4bc5-86bb-db911c8d5738'
    // Note that this account token should belong to a new Account
    config.newaccountToken = '1cb7d8f6-a01a-4ed1-b52f-6a96c8c7a91f'
  }

   return config;
}