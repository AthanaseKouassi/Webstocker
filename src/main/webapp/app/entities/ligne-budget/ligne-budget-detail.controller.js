(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneBudgetDetailController', LigneBudgetDetailController);

    LigneBudgetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LigneBudget', 'Budget', 'Mission'];

    function LigneBudgetDetailController($scope, $rootScope, $stateParams, entity, LigneBudget, Budget, Mission) {
        var vm = this;
        vm.ligneBudget = entity;
        vm.load = function (id) {
            LigneBudget.get({id: id}, function(result) {
                vm.ligneBudget = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:ligneBudgetUpdate', function(event, result) {
            vm.ligneBudget = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
