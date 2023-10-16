(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Inventaire', Inventaire);

    Inventaire.$inject = ['$resource', 'DateUtils'];

    function Inventaire ($resource, DateUtils) {
        var resourceUrl =  'api/inventaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateInventaire = DateUtils.convertLocalDateFromServer(data.dateInventaire);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateInventaire = DateUtils.convertLocalDateToServer(copy.dateInventaire);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateInventaire = DateUtils.convertLocalDateToServer(copy.dateInventaire);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
