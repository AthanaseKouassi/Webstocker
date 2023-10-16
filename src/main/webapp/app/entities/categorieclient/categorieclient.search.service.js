(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('CategorieclientSearch', CategorieclientSearch);

    CategorieclientSearch.$inject = ['$resource'];

    function CategorieclientSearch($resource) {
        var resourceUrl =  'api/_search/categorieclients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
