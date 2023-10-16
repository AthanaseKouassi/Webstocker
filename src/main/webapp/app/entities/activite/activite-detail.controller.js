(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ActiviteDetailController', ActiviteDetailController);

    ActiviteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Activite', 'LigneMissionActivite', 'TypeActivite'];

    function ActiviteDetailController($scope, $rootScope, $stateParams, entity, Activite, LigneMissionActivite, TypeActivite) {
        var vm = this;
        vm.activite = entity;
        vm.load = function (id) {
            Activite.get({id: id}, function(result) {
                vm.activite = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:activiteUpdate', function(event, result) {
            vm.activite = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
