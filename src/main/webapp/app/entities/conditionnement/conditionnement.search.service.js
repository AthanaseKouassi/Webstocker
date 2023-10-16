(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('ConditionnementSearch', ConditionnementSearch);

    ConditionnementSearch.$inject = ['$resource'];

    function ConditionnementSearch($resource) {
        var resourceUrl =  'api/_search/conditionnements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
