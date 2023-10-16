(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('BonDeSortie', BonDeSortie);

    BonDeSortie.$inject = ['$resource', 'DateUtils'];

    function BonDeSortie ($resource, DateUtils) {
        var resourceUrl =  'api/bon-de-sorties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.daateCreation = DateUtils.convertLocalDateFromServer(data.daateCreation);

                        data.dateReception = DateUtils.convertLocalDateFromServer(data.dateReception);

                        data.dateReceptionTransfert = DateUtils.convertLocalDateFromServer(data.dateReceptionTransfert);

                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.daateCreation = DateUtils.convertLocalDateToServer(copy.daateCreation);

                    copy.dateReception = DateUtils.convertLocalDateToServer(copy.dateReception);

                    copy.dateReceptionTransfert = DateUtils.convertLocalDateToServer(copy.dateReceptionTransfert);

                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.daateCreation = DateUtils.convertLocalDateToServer(copy.daateCreation);

                    copy.dateReception = DateUtils.convertLocalDateToServer(copy.dateReception);

                    copy.dateReceptionTransfert = DateUtils.convertLocalDateToServer(copy.dateReceptionTransfert);

                    return angular.toJson(copy);
                }
            }
        });
    }
})();
