var app = angular.module('HR App', ['ngRoute']);

app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '/app/components/home/home.html',
        })
        .when('/employees', {
            templateUrl: '/app/components/employees/employees.html',
            controller: 'EmployeesController'
        }).otherwise({
            redirectTo: '/home'
        });
}]);

app.controller('EmployeesController', ['$scope', '$http', function ($scope, $http) {

    $scope.employees = [];
    
    $http({
        method: 'GET',
        url: 'http://localhost:4567/payrollservice/employees/'

        }).then(function successCallback (response) {
            $scope.employees = response.data;

        }, function errorCallback () {
            console.log('Could not perform GET');

    });

    $scope.addEmployee = function () {
        
            $http({
                method: 'POST',
                url: 'http://localhost:4567/payrollservice/employees/',
                data: JSON.stringify($scope.employee),
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }

                }).then(function successCallback (response) {
                    console.log('POST successful');
                    console.log(response.data);
        
                }, function errorCallback () {
                    console.log('Could not perform POST');
        
            });

        $scope.employee.employee_id = "";
        $scope.employee.first_name = "";
        $scope.employee.last_name = "";
        $scope.employee.salary = "";
        $scope.employee.new_hire = true;

    };

}]);