(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('LigneMissionActiviteSearch', LigneMissionActiviteSearch);

    LigneMissionActiviteSearch.$inject = ['$resource'];

    function LigneMissionActiviteSearch($resource) {
        var resourceUrl =  'api/_search/ligne-mission-activites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
