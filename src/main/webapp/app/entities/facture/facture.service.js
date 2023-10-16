(function() {
    'use strict';
    angular
        .module('webstockerApp')
        .factory('Facture', Facture);

    Facture.$inject = ['$resource', 'DateUtils'];

    function Facture ($resource, DateUtils) {
        var resourceUrl =  'api/factures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateFacture = DateUtils.convertLocalDateFromServer(data.dateFacture);
                    data.dateLimitePaiement = DateUtils.convertLocalDateFromServer(data.dateLimitePaiement);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateFacture = DateUtils.convertLocalDateToServer(data.dateFacture);
                    data.dateLimitePaiement = DateUtils.convertLocalDateToServer(data.dateLimitePaiement);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateFacture = DateUtils.convertLocalDateToServer(data.dateFacture);
                    data.dateLimitePaiement = DateUtils.convertLocalDateToServer(data.dateLimitePaiement);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
