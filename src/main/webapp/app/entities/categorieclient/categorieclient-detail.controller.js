(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CategorieclientDetailController', CategorieclientDetailController);

    CategorieclientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Categorieclient', 'Client', 'Prix'];

    function CategorieclientDetailController($scope, $rootScope, $stateParams, entity, Categorieclient, Client, Prix) {
        var vm = this;
        vm.categorieclient = entity;
        vm.load = function (id) {
            Categorieclient.get({id: id}, function(result) {
                vm.categorieclient = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:categorieclientUpdate', function(event, result) {
            vm.categorieclient = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
