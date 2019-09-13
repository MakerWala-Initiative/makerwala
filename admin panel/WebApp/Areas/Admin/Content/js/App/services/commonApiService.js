var app = angular.module('app');

app.service('commonApiService', function ($http, $q, pageLimit, path, $cookieStore, $state, $rootScope) {

    var res_obj = {};

    this.callApi = function (req) {
        var deferred = $q.defer();
        if (!req.obj) req.obj = {};
        req.obj.userId = $cookieStore.get('id');
        var currentState = $state.current.name;

        if (currentState != 'app.company' && currentState != 'app.department' && currentState != 'app.useraddupdate' && currentState != 'app.users') {
            req.obj.company_id = $cookieStore.get('company_id');
            req.obj.department_id = $cookieStore.get('department_id');
            req.obj.year_id = $cookieStore.get('year_id');
        }


        var header = {
            headers: {}
        }
        var token = localStorage["accessToken"];
        if (token) {
            header['headers']['Authorization'] = 'Bearer ' + token;
        }

        return $http[req.method](path.API_PATH + req.api, req.obj, header)
            .then(function (response) {
                if (req.obj && req.obj.limit && response.data && response.data.flag && response.data.data && response.data.data.list && response.data.data.list.length) {
                    var totalCount = response.data.data.count;
                    var limits = [];
                    var pagelimitlist;
                    if ($rootScope.pageLimitList && $rootScope.pageLimitList.length > 0)
                        pagelimitlist = $rootScope.pageLimitList;
                    else
                        pagelimitlist = angular.copy(pageLimit);//if page setting not exist in db than use pagelimit constant setting
                    var allcount;
                    _.each(pagelimitlist, function (val, key) {
                        // var is_show = false;
                        if (totalCount > val) limits.push({ name: val, page: val, is_show: true });
                        else if (!allcount) allcount = val;
                        //limits.push({ name: val, page: val, is_show: is_show });
                    });
                    limits.push({ name: 'ALL', page: allcount, is_show: true });
                    response.data.data['limits'] = angular.copy(limits);
                }
                deferred.resolve(response.data);
                // promise is returned
                return deferred.promise;

            }, function (response) {
                deferred.resolve({ flag: false, message: response.status + ' | ' + response.statusText });

                // deferred.reject(res_obj);
                // promise is returned
                return deferred.promise;
            })


    }

    this.roundOff = function (value, decimal) {
        return Math.round(parseFloat((value * Math.pow(10, 2)).toFixed(decimal))) / Math.pow(10, 2);
    }
}
)

app.service('stateParamService', function () {
    var parameterList = [];

    var addParameters = function (newObj) {
        parameterList = newObj;
    };

    var getParameters = function () {
        return parameterList;
    };

    return {
        addParameter: addParameters,
        getParameter: getParameters
    };

});

app.service("SignalrService", function () {
    var notificationHubProxy = null;

    this.initialize = function () {
        alert()
        $.connection.hub.logging = true;
        notificationHubProxy = $.connection.notificationHub;

        notificationHubProxy.client.hello = function () {
            console.log("Hello from ASP.NET Web API");
        };

        $.connection.hub.start().done(function () {
            console.log("started");
        }).fail(function (result) {
            console.log(result);
        });
    };
});