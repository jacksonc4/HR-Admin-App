angular
    .module('app')
    .factory('apiFactory', apiFactory);

    apiFactory.$inject = [ '$http' ];
    
    function apiFactory ($http) {
    
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
    
    };