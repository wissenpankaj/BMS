# OpenApiDefinition.BatteryFaultControllerApi

All URIs are relative to *http://localhost:8081*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getBatteryFaults**](BatteryFaultControllerApi.md#getBatteryFaults) | **GET** /api/faults | 



## getBatteryFaults

> [BatteryFaultModel] getBatteryFaults(opts)



### Example

```javascript
import OpenApiDefinition from 'open_api_definition';

let apiInstance = new OpenApiDefinition.BatteryFaultControllerApi();
let opts = {
  'faultId': "faultId_example", // String | 
  'gps': "gps_example", // String | 
  'vehicleId': "vehicleId_example", // String | 
  'batteryId': "batteryId_example", // String | 
  'faultReason': "faultReason_example", // String | 
  'recommendation': "recommendation_example", // String | 
  'level': "level_example", // String | 
  'risk': "risk_example", // String | 
  '_date': "_date_example" // String | 
};
apiInstance.getBatteryFaults(opts, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **faultId** | **String**|  | [optional] 
 **gps** | **String**|  | [optional] 
 **vehicleId** | **String**|  | [optional] 
 **batteryId** | **String**|  | [optional] 
 **faultReason** | **String**|  | [optional] 
 **recommendation** | **String**|  | [optional] 
 **level** | **String**|  | [optional] 
 **risk** | **String**|  | [optional] 
 **_date** | **String**|  | [optional] 

### Return type

[**[BatteryFaultModel]**](BatteryFaultModel.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

