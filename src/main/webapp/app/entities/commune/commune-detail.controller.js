(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CommuneDetailController', CommuneDetailController);

    CommuneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Commune', 'Ville'];

    function CommuneDetailController($scope, $rootScope, $stateParams, entity, Commune, Ville) {
        var vm = this;
        vm.commune = entity;
        vm.load = function (id) {
            Commune.get({id: id}, function(result) {
                vm.commune = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:communeUpdate', function(event, result) {
            vm.commune = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
