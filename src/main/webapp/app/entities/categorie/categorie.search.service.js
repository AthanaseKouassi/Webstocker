(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .factory('CategorieSearch', CategorieSearch);

    CategorieSearch.$inject = ['$resource'];

    function CategorieSearch($resource) {
        var resourceUrl =  'api/_search/categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
