var app = angular.module('HR App', ['ngRoute']);

app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        .when('/home', {
            templateUrl: '/app/components/home/home.html',
        })
        .when('/employees', {
            templateUrl: '/app/components/employees/employees.html',
            controller: 'EmployeesController'
        }).when('/employees/:employee_id', {
            templateUrl: '/app/components/employees/edit_employee.html',
            controller: 'EmployeesController'
        }).otherwise({
            redirectTo: '/home'
        });
}]);

app.factory('apiFactory', ['$http', function ($http) {

    var api_context = "http://localhost:4567/payrollservice/employees/";
    var apiFactory = {};

    apiFactory.getEmployees = function () {
        return $http.get(api_context);
    };

    apiFactory.getEmployee = function (id) {
        return $http.get(api_context + '/' + id);
    };

    apiFactory.addEmployee = function (employee) {
        return $http({
            method: 'POST',
            url: 'http://localhost:4567/payrollservice/employees/',
            data: JSON.stringify(employee),
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });

    };

    apiFactory.removeEmployee = function (id) {
        return $http.delete(api_context + '/' + id);
    };

    return apiFactory;

}]);

app.controller('EmployeesController', ['$scope', 'apiFactory', function ($scope, apiFactory) {

    $scope.employees = [];

    init();

    function init () {
        apiFactory.getEmployees()
            .then(function successCallback (response) {
                $scope.employees = response.data;
            }, function errorCallback (error) {
                console.log('Unable to load data: ' + error.message);
            });

    };

    $scope.removeEmployee = function (employee) {
        apiFactory.removeEmployee(employee.employee_id)
            .then(function successCallback (response) {
                init();
                console.log(employee.first_name + ' will be removed from payroll.');
                console.log(response.data);
            }, function errorCallback (error) {
                console.log('Could not DELETE employee: ' + error.message);
            });
    };

    $scope.addEmployee = function () {

        apiFactory.addEmployee($scope.employee)
            .then(function successCallback (response) {
                init();
                console.log('POST successful: ' + response.data);
            }, function errorCallback () {
                console.log('Could not POST data');
            });

            $scope.employee.employee_id = "";
            $scope.employee.first_name = "";
            $scope.employee.last_name = "";
            $scope.employee.salary = "";
            $scope.employee.hire_date = "";

    };

}]);