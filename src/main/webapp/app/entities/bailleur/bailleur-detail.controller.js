(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('BailleurDetailController', BailleurDetailController);

    BailleurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Bailleur', 'Commande'];

    function BailleurDetailController($scope, $rootScope, $stateParams, entity, Bailleur, Commande) {
        var vm = this;
        vm.bailleur = entity;
        vm.load = function (id) {
            Bailleur.get({id: id}, function(result) {
                vm.bailleur = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:bailleurUpdate', function(event, result) {
            vm.bailleur = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
