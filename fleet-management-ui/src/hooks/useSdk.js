

import BatteryFaultControllerApi from "../sdk/faultyBattery/src/api/BatteryFaultControllerApi";




export const useSdk = (baseUrl, token) => {
  // Configure the SDK instance
  // faultyBatteryApi.apiClient.basePath = baseUrl;
  //   faultyBatteryApi.apiClient.defaultHeaders.Authorization = `Bearer ${token}`;
  const faultyBatteryObj = new BatteryFaultControllerApi();
console.log("here", faultyBatteryObj);
var callback = function(error, data, response) {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + JSON.stringify(data));
  }
};
console.log("get ", faultyBatteryObj.getBatteryFaults({},callback));

  return {
    // faultyBatteryApi,
  };
};
