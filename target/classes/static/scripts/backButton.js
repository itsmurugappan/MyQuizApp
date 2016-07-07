history.pushState(null, null, $(location).attr('href'));
    window.addEventListener('popstate', function () {
        history.pushState(null, null, $(location).attr('href'));
    });