/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 *
 */

import ApiClient from '../ApiClient';

/**
 * The BatteryFaultModel model module.
 * @module //BatteryFaultModel
 * @version v0
 */
class BatteryFaultModel {
    /**
     * Constructs a new <code>BatteryFaultModel</code>.
     * @alias module://BatteryFaultModel
     * @param gps {String} 
     * @param vehicleId {String} 
     * @param batteryId {String} 
     * @param faultReason {String} 
     * @param recommendation {String} 
     * @param time {Date} 
     * @param level {String} 
     * @param risk {String} 
     */
    constructor(gps, vehicleId, batteryId, faultReason, recommendation, time, level, risk) { 
        
        BatteryFaultModel.initialize(this, gps, vehicleId, batteryId, faultReason, recommendation, time, level, risk);
    }

    /**
     * Initializes the fields of this object.
     * This method is used by the constructors of any subclasses, in order to implement multiple inheritance (mix-ins).
     * Only for internal use.
     */
    static initialize(obj, gps, vehicleId, batteryId, faultReason, recommendation, time, level, risk) { 
        obj['gps'] = gps;
        obj['vehicleId'] = vehicleId;
        obj['batteryId'] = batteryId;
        obj['faultReason'] = faultReason;
        obj['recommendation'] = recommendation;
        obj['time'] = time;
        obj['level'] = level;
        obj['risk'] = risk;
    }

    /**
     * Constructs a <code>BatteryFaultModel</code> from a plain JavaScript object, optionally creating a new instance.
     * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
     * @param {Object} data The plain JavaScript object bearing properties of interest.
     * @param {module://BatteryFaultModel} obj Optional instance to populate.
     * @return {module://BatteryFaultModel} The populated <code>BatteryFaultModel</code> instance.
     */
    static constructFromObject(data, obj) {
        if (data) {
            obj = obj || new BatteryFaultModel();

            if (data.hasOwnProperty('gps')) {
                obj['gps'] = ApiClient.convertToType(data['gps'], 'String');
            }
            if (data.hasOwnProperty('vehicleId')) {
                obj['vehicleId'] = ApiClient.convertToType(data['vehicleId'], 'String');
            }
            if (data.hasOwnProperty('batteryId')) {
                obj['batteryId'] = ApiClient.convertToType(data['batteryId'], 'String');
            }
            if (data.hasOwnProperty('faultReason')) {
                obj['faultReason'] = ApiClient.convertToType(data['faultReason'], 'String');
            }
            if (data.hasOwnProperty('recommendation')) {
                obj['recommendation'] = ApiClient.convertToType(data['recommendation'], 'String');
            }
            if (data.hasOwnProperty('time')) {
                obj['time'] = ApiClient.convertToType(data['time'], 'Date');
            }
            if (data.hasOwnProperty('level')) {
                obj['level'] = ApiClient.convertToType(data['level'], 'String');
            }
            if (data.hasOwnProperty('risk')) {
                obj['risk'] = ApiClient.convertToType(data['risk'], 'String');
            }
            if (data.hasOwnProperty('faultId')) {
                obj['faultId'] = ApiClient.convertToType(data['faultId'], 'String');
            }
        }
        return obj;
    }


}

/**
 * @member {String} gps
 */
BatteryFaultModel.prototype['gps'] = undefined;

/**
 * @member {String} vehicleId
 */
BatteryFaultModel.prototype['vehicleId'] = undefined;

/**
 * @member {String} batteryId
 */
BatteryFaultModel.prototype['batteryId'] = undefined;

/**
 * @member {String} faultReason
 */
BatteryFaultModel.prototype['faultReason'] = undefined;

/**
 * @member {String} recommendation
 */
BatteryFaultModel.prototype['recommendation'] = undefined;

/**
 * @member {Date} time
 */
BatteryFaultModel.prototype['time'] = undefined;

/**
 * @member {String} level
 */
BatteryFaultModel.prototype['level'] = undefined;

/**
 * @member {String} risk
 */
BatteryFaultModel.prototype['risk'] = undefined;

/**
 * @member {String} faultId
 */
BatteryFaultModel.prototype['faultId'] = undefined;






export default BatteryFaultModel;

