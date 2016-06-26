(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('personlocation', {
            parent: 'entity',
            url: '/personlocation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fadsiiApp.personlocation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personlocation/personlocations.html',
                    controller: 'PersonlocationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personlocation');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('personlocation-detail', {
            parent: 'entity',
            url: '/personlocation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fadsiiApp.personlocation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personlocation/personlocation-detail.html',
                    controller: 'PersonlocationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personlocation');
                    $translatePartialLoader.addPart('status');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Personlocation', function($stateParams, Personlocation) {
                    return Personlocation.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('personlocation.new', {
            parent: 'personlocation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personlocation/personlocation-dialog.html',
                    controller: 'PersonlocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isprimary: false,
                                status: null,
                                lastmodifiedby: null,
                                lastmodifieddatetime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('personlocation', null, { reload: true });
                }, function() {
                    $state.go('personlocation');
                });
            }]
        })
        .state('personlocation.edit', {
            parent: 'personlocation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personlocation/personlocation-dialog.html',
                    controller: 'PersonlocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Personlocation', function(Personlocation) {
                            return Personlocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personlocation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personlocation.delete', {
            parent: 'personlocation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personlocation/personlocation-delete-dialog.html',
                    controller: 'PersonlocationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Personlocation', function(Personlocation) {
                            return Personlocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personlocation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
