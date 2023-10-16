(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('CommuneSearch', CommuneSearch);

    CommuneSearch.$inject = ['$resource'];

    function CommuneSearch($resource) {
        var resourceUrl =  'api/_search/communes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
