(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ConditionnementDetailController', ConditionnementDetailController);

    ConditionnementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Conditionnement', 'Produit'];

    function ConditionnementDetailController($scope, $rootScope, $stateParams, entity, Conditionnement, Produit) {
        var vm = this;
        vm.conditionnement = entity;
        vm.load = function (id) {
            Conditionnement.get({id: id}, function(result) {
                vm.conditionnement = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:conditionnementUpdate', function(event, result) {
            vm.conditionnement = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
