'use strict';

/**
 * @author: phuongds
 */
let app = angular.module('app', ['ui.grid','ui.grid.selection', 'ui.grid.cellNav','ui.grid.exporter','ui.grid.pagination','ngStorage']);
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
app.directive('url',validUrl);
function validUrl(){
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ctrl) {
            function doValid(ngModelValue) {
                let pattern = /^(?:(?:https?|ftp):\/\/)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})))(?::\d{2,5})?(?:\/\S*)?$/;
                if (pattern.test(ngModelValue)) {
                    ctrl.$setValidity('pattern', true);
                } else {
                    ctrl.$setValidity('pattern', false);
                }
                return ngModelValue;
            }
            ctrl.$parsers.push(doValid);
        }
    };
}

//username validator
app.directive('unique',validUnique);
function validUnique($timeout,$http){
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ctrl) {
            function isExist(ngModelValue) {
                let url = '/account/api/isExist';
                let data  = {'name':`${element[0].name}`,'value':ngModelValue};
                return $http.post(url,data);
            }
            function doValid(ngModelValue) {
                let xhr = isExist(ngModelValue);
                xhr.then((res) => {
                    $timeout(function () {
                        let isExist = res.data.success;
                        if(isExist==="0"){
                            ctrl.$setValidity('duplicate', true);
                        }
                        else{
                            ctrl.$setValidity('duplicate', false);
                        }
                    });
                });
                return ngModelValue;
            }
            ctrl.$parsers.push(doValid);
        }
    };
}


//email validator
app.directive('email',validEmail);
function validEmail(){
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ctrl) {
            function doValid(ngModelValue) {
                let pattern = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
                if (pattern.test(ngModelValue)) {
                    ctrl.$setValidity('pattern', true);
                } else {
                    ctrl.$setValidity('pattern', false);
                }
                return ngModelValue;
            }
            ctrl.$parsers.push(doValid);
        }
    };
}

//password validator

app.directive('password',validPassword);
function validPassword(){
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attr, ctrl) {
            function doValid(ngModelValue) {
                let pattern = /^(?=.*\d).{4,8}$/;
                if (pattern.test(ngModelValue)) {
                    ctrl.$setValidity('pattern', true);
                } else {
                    ctrl.$setValidity('pattern', false);
                }
                return ngModelValue;
            }
            ctrl.$parsers.push(doValid);
        }
    };
}
