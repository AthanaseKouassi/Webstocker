(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Commande', Commande);

    Commande.$inject = ['$resource', 'DateUtils'];

    function Commande ($resource, DateUtils) {
        var resourceUrl =  'api/commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCommande = DateUtils.convertLocalDateFromServer(data.dateCommande);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateCommande = DateUtils.convertLocalDateToServer(data.dateCommande);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateCommande = DateUtils.convertLocalDateToServer(data.dateCommande);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
