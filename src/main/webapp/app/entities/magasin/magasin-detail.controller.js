(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('MagasinDetailController', MagasinDetailController);

    MagasinDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Magasin', 'Localite', 'Livraison'];

    function MagasinDetailController($scope, $rootScope, $stateParams, entity, Magasin, Localite, Livraison) {
        var vm = this;
        vm.magasin = entity;
        vm.load = function (id) {
            Magasin.get({id: id}, function(result) {
                vm.magasin = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:magasinUpdate', function(event, result) {
            vm.magasin = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
