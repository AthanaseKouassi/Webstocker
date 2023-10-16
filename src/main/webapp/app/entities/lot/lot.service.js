(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Lot', Lot);

    Lot.$inject = ['$resource', 'DateUtils'];

    function Lot ($resource, DateUtils) {
        var resourceUrl =  'api/lots/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateFabrication = DateUtils.convertLocalDateFromServer(data.dateFabrication);
                    data.datePeremption = DateUtils.convertLocalDateFromServer(data.datePeremption);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateFabrication = DateUtils.convertLocalDateToServer(data.dateFabrication);
                    data.datePeremption = DateUtils.convertLocalDateToServer(data.datePeremption);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateFabrication = DateUtils.convertLocalDateToServer(data.dateFabrication);
                    data.datePeremption = DateUtils.convertLocalDateToServer(data.datePeremption);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
