
import BatteryFaultControllerApi from "../sdk/faultyBattery/src/api/BatteryFaultControllerApi";


const initializeBatteryApi = (token) => {
  const apiClient = new BatteryFaultControllerApi();
  //baseUrl = "http://localhost:8085/"
  //apiClient.apiClient.basePath = baseUrl;
  //apiClient.apiClient.defaultHeaders.Authorization = `Bearer ${token}`;
  return apiClient;
};

export const getBatteryFaults = async (token) => {
  const api = initializeBatteryApi(token);
  return new Promise((resolve, reject) => {
    api.getBatteryFaults({}, (error, data, response) => {
      if (error) {
        reject(error);
      } else {
        resolve(data);
      }
    });
  });
};
