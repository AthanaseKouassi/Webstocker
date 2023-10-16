(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('LigneMissionActivite', LigneMissionActivite);

    LigneMissionActivite.$inject = ['$resource', 'DateUtils'];

    function LigneMissionActivite ($resource, DateUtils) {
        var resourceUrl =  'api/ligne-mission-activites/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateResultat = DateUtils.convertLocalDateFromServer(data.dateResultat);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateResultat = DateUtils.convertLocalDateToServer(data.dateResultat);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateResultat = DateUtils.convertLocalDateToServer(data.dateResultat);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
