(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('MissionSearch', MissionSearch);

    MissionSearch.$inject = ['$resource'];

    function MissionSearch($resource) {
        var resourceUrl =  'api/_search/missions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
