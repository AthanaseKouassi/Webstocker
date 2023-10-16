(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Objectifs', Objectifs);

    Objectifs.$inject = ['$resource', 'DateUtils'];

    function Objectifs ($resource, DateUtils) {
        var resourceUrl =  'api/objectifs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.periode = DateUtils.convertLocalDateFromServer(data.periode);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.periode = DateUtils.convertLocalDateToServer(data.periode);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.periode = DateUtils.convertLocalDateToServer(data.periode);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
