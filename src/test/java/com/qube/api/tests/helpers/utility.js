function () {
    // This below method can be used to generate a random filename
    function randomText(length) {
        var text = "";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (var i = 0; i < length; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        return text;
    }

    function wait(waitTime) {
             karate.log('sleeping');
             java.lang.Thread.sleep(waitTime);
    }

    return {
        randomText: randomText,
        wait: wait,
    };
}
