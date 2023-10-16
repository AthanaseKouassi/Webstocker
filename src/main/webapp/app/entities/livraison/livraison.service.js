(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Livraison', Livraison);

    Livraison.$inject = ['$resource', 'DateUtils'];

    function Livraison ($resource, DateUtils) {
        var resourceUrl =  'api/livraisons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateLivraison = DateUtils.convertLocalDateFromServer(data.dateLivraison);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateLivraison = DateUtils.convertLocalDateToServer(data.dateLivraison);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateLivraison = DateUtils.convertLocalDateToServer(data.dateLivraison);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
