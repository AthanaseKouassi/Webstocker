(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneobjectifventeDetailController', LigneobjectifventeDetailController);

    LigneobjectifventeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Ligneobjectifvente', 'Objectifs', 'Produit'];

    function LigneobjectifventeDetailController($scope, $rootScope, $stateParams, entity, Ligneobjectifvente, Objectifs, Produit) {
        var vm = this;
        vm.ligneobjectifvente = entity;
        vm.load = function (id) {
            Ligneobjectifvente.get({id: id}, function(result) {
                vm.ligneobjectifvente = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:ligneobjectifventeUpdate', function(event, result) {
            vm.ligneobjectifvente = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
