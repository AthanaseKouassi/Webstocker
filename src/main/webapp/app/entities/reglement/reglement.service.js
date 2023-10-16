(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Reglement', Reglement);

    Reglement.$inject = ['$resource', 'DateUtils'];

    function Reglement ($resource, DateUtils) {
        var resourceUrl =  'api/reglements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateReglement = DateUtils.convertLocalDateFromServer(data.dateReglement);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateReglement = DateUtils.convertLocalDateToServer(data.dateReglement);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateReglement = DateUtils.convertLocalDateToServer(data.dateReglement);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
