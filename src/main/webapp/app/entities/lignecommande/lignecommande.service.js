(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Lignecommande', Lignecommande);

    Lignecommande.$inject = ['$resource', 'DateUtils'];

    function Lignecommande ($resource, DateUtils) {
        var resourceUrl =  'api/lignecommandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateFabrication = DateUtils.convertLocalDateFromServer(data.dateFabrication);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateFabrication = DateUtils.convertLocalDateToServer(data.dateFabrication);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateFabrication = DateUtils.convertLocalDateToServer(data.dateFabrication);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
