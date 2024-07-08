  config.set({
//    "singleRun": false,
//    "autoWatch": true,
    client: {
      mocha: {
        timeout : 15000 // 6 seconds - upped from 2 seconds
      }
    }
  });
//config.loggers.push({type: 'console'})
config.logLevel = config.LOG_DEBUG