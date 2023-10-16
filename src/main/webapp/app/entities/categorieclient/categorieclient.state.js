(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('categorieclient', {
            parent: 'entity',
            url: '/categorieclient',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.categorieclient.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorieclient/categorieclients.html',
                    controller: 'CategorieclientController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('categorieclient');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('categorieclient-detail', {
            parent: 'entity',
            url: '/categorieclient/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.categorieclient.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/categorieclient/categorieclient-detail.html',
                    controller: 'CategorieclientDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('categorieclient');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Categorieclient', function($stateParams, Categorieclient) {
                    return Categorieclient.get({id : $stateParams.id});
                }]
            }
        })
        .state('categorieclient.new', {
            parent: 'categorieclient',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorieclient/categorieclient-dialog.html',
                    controller: 'CategorieclientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelleCategorieclient: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('categorieclient', null, { reload: true });
                }, function() {
                    $state.go('categorieclient');
                });
            }]
        })
        .state('categorieclient.edit', {
            parent: 'categorieclient',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorieclient/categorieclient-dialog.html',
                    controller: 'CategorieclientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Categorieclient', function(Categorieclient) {
                            return Categorieclient.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorieclient', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('categorieclient.delete', {
            parent: 'categorieclient',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/categorieclient/categorieclient-delete-dialog.html',
                    controller: 'CategorieclientDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Categorieclient', function(Categorieclient) {
                            return Categorieclient.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('categorieclient', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
