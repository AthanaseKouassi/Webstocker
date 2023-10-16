(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('RegionDetailController', RegionDetailController);

    RegionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Region'];

    function RegionDetailController($scope, $rootScope, $stateParams, entity, Region) {
        var vm = this;
        vm.region = entity;
        vm.load = function (id) {
            Region.get({id: id}, function(result) {
                vm.region = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:regionUpdate', function(event, result) {
            vm.region = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
