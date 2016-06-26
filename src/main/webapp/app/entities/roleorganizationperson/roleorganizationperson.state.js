(function() {
    'use strict';

    angular
        .module('fadsiiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('roleorganizationperson', {
            parent: 'entity',
            url: '/roleorganizationperson?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fadsiiApp.roleorganizationperson.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/roleorganizationperson/roleorganizationpeople.html',
                    controller: 'RoleorganizationpersonController',
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
                    $translatePartialLoader.addPart('roleorganizationperson');
                    $translatePartialLoader.addPart('status');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('roleorganizationperson-detail', {
            parent: 'entity',
            url: '/roleorganizationperson/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'fadsiiApp.roleorganizationperson.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/roleorganizationperson/roleorganizationperson-detail.html',
                    controller: 'RoleorganizationpersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('roleorganizationperson');
                    $translatePartialLoader.addPart('status');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Roleorganizationperson', function($stateParams, Roleorganizationperson) {
                    return Roleorganizationperson.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('roleorganizationperson.new', {
            parent: 'roleorganizationperson',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roleorganizationperson/roleorganizationperson-dialog.html',
                    controller: 'RoleorganizationpersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                role: null,
                                status: null,
                                lastmodifiedby: null,
                                lastmodifieddatetime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('roleorganizationperson', null, { reload: true });
                }, function() {
                    $state.go('roleorganizationperson');
                });
            }]
        })
        .state('roleorganizationperson.edit', {
            parent: 'roleorganizationperson',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roleorganizationperson/roleorganizationperson-dialog.html',
                    controller: 'RoleorganizationpersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Roleorganizationperson', function(Roleorganizationperson) {
                            return Roleorganizationperson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('roleorganizationperson', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('roleorganizationperson.delete', {
            parent: 'roleorganizationperson',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/roleorganizationperson/roleorganizationperson-delete-dialog.html',
                    controller: 'RoleorganizationpersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Roleorganizationperson', function(Roleorganizationperson) {
                            return Roleorganizationperson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('roleorganizationperson', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
