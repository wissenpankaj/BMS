import FaultyBatterySdk from './sdk/faultyBattery/src';

const faultyBatteryApi = new FaultyBatterySdk.BatteryFaultControllerApi();

export const useSdk = (baseUrl, token) => {
  // Configure the SDK instance
  faultyBatteryApi.apiClient.basePath = baseUrl;
//   faultyBatteryApi.apiClient.defaultHeaders.Authorization = `Bearer ${token}`;

  return {
    faultyBatteryApi,
  };
};
