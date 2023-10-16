(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Mission', Mission);

    Mission.$inject = ['$resource', 'DateUtils'];

    function Mission ($resource, DateUtils) {
        var resourceUrl =  'api/missions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateDebut = DateUtils.convertLocalDateFromServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocalDateFromServer(data.dateFin);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocalDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocalDateToServer(data.dateFin);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateDebut = DateUtils.convertLocalDateToServer(data.dateDebut);
                    data.dateFin = DateUtils.convertLocalDateToServer(data.dateFin);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
