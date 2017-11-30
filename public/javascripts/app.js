'use strict';

/**
 * @author: phuongds
 */
let app = angular.module('app', ['ui.grid','ui.grid.selection', 'ui.grid.cellNav','ui.grid.exporter','ui.grid.pagination']);
app.filter('mapGender', function () {
    let hash = {
        '1': "male",
        '2': "female",
        '3': "other",
    };

    return function (input) {
        return hash[input];
    };
});
//website validator
app.directive('checkValidUrl',checkValidUrl);
function checkValidUrl($timeout,$http){
    return {
        restrict: 'A',
        // require NgModelController, i.e. require a controller of ngModel directive
        require: 'ngModel',
        // create linking function and pass in our NgModelController as a 4th argument
        link: function(scope, element, attr, ctrl) {
            function isExist(ngModelValue) {
                let url = '/backend/article/checkURLExist/'+ngModelValue;
                return $http.get(url);
            }
            function doValid(ngModelValue) {
                if (/^[a-zA-Z0-9_-]*$/.test(ngModelValue)) {
                    ctrl.$setValidity('pattern', true);
                } else {
                    ctrl.$setValidity('pattern', false);
                }
                if (ngModelValue.length <= 30) {
                    ctrl.$setValidity('maxLength', true);
                } else {
                    ctrl.$setValidity('maxLength', false);
                }
                let xhr = isExist(ngModelValue);
                xhr.then((res) => {
                    $timeout(function () {
                        let isExist = res.data;
                        if(isExist==="0"){
                            ctrl.$setValidity('unique', true);
                        }
                        else{
                            ctrl.$setValidity('unique', false);
                        }
                    });
                });

                // we need to return our ngModelValue, to be displayed to the user(value of the input)
                return ngModelValue;
            }

            // we need to add our doValid function to an array of other(build-in or custom) functions
            // I have not notice any performance issues, but it would be worth investigating how much
            // effect does this have on the performance of the app
            ctrl.$parsers.push(doValid);
        }
    };
}
checkValidUrl.$inject = ['$timeout','$http'];

//username validator
app.directive('checkUnique',checkUnique);
function checkUnique($timeout,$http){
    return {
        restrict: 'A',
        // require NgModelController, i.e. require a controller of ngModel directive
        require: 'ngModel',
        // create linking function and pass in our NgModelController as a 4th argument
        link: function(scope, element, attr, ctrl) {
            console.log(scope);
            function isExist(ngModelValue) {
                let url = '/account/api/isExist';
                let data  = {'name':"email",'value':ngModelValue};
                return $http.post(url,data);
            }
            function doValid(ngModelValue) {
                if (/^[a-zA-Z0-9_-]*$/.test(ngModelValue)) {
                    ctrl.$setValidity('pattern', true);
                } else {
                    ctrl.$setValidity('pattern', false);
                }
                if (ngModelValue.length <= 30) {
                    ctrl.$setValidity('maxLength', true);
                } else {
                    ctrl.$setValidity('maxLength', false);
                }
                let xhr = isExist(ngModelValue);
                xhr.then((res) => {
                    $timeout(function () {
                        let isExist = res.data;
                        if(isExist==="0"){
                            ctrl.$setValidity('unique', true);
                        }
                        else{
                            ctrl.$setValidity('unique', false);
                        }
                    });
                });

                // we need to return our ngModelValue, to be displayed to the user(value of the input)
                return ngModelValue;
            }

            // we need to add our doValid function to an array of other(build-in or custom) functions
            // I have not notice any performance issues, but it would be worth investigating how much
            // effect does this have on the performance of the app
            ctrl.$parsers.push(doValid);
        }
    };
}
checkUnique.$inject = ['$timeout','$http'];

//email validator



