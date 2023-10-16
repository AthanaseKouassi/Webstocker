(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Prix', Prix);

    Prix.$inject = ['$resource', 'DateUtils'];

    function Prix ($resource, DateUtils) {
        var resourceUrl =  'api/prixes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateFixation = DateUtils.convertLocalDateFromServer(data.dateFixation);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateFixation = DateUtils.convertLocalDateToServer(copy.dateFixation);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateFixation = DateUtils.convertLocalDateToServer(copy.dateFixation);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
