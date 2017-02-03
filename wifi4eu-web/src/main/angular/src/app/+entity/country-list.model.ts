import {CountryDetails} from './country-details.model';
export class CountryList {
  private countryList : CountryDetails[];

  constructor() {
    this.countryList = [
      {
        "name": "Austria",
        "code": "AT"
      },{
        "name": "Belgium",
        "code": "BE"
      },{
        "name": "Bulgaria",
        "code": "BG"
      },{
        "name": "Cyprus",
        "code": "CY"
      }, {
        "name": "Czech Republic",
        "code": "CZ"
      }, {
        "name": "Denmark",
        "code": "DK"
      },{
        "name": "Estonia",
        "code": "EE"
      },{
        "name": "Finland",
        "code": "FI"
      }, {
        "name": "France",
        "code": "FR"
      },{
        "name": "Germany",
        "code": "DE"
      }, {
        "name": "Greece",
        "code": "GR"
      },{
        "name": "Hungary",
        "code": "HU"
      }, {
        "name": "Iceland",
        "code": "IS"
      }, {
        "name": "Ireland",
        "code": "IE"
      }, {
        "name": "Italy",
        "code": "IT"
      }, {
        "name": "Lithuania",
        "code": "LT"
      }, {
        "name": "Luxembourg",
        "code": "LU"
      },{
        "name": "Malta",
        "code": "MT"
      }, {
        "name": "Netherlands",
        "code": "NL"
      }, {
        "name": "Norway",
        "code": "NO"
      }, {
        "name": "Poland",
        "code": "PL"
      }, {
        "name": "Portugal",
        "code": "PT"
      },{
        "name": "Romania",
        "code": "RO"
      }, {
        "name": "Slovakia",
        "code": "SK"
      }, {
        "name": "Slovenia",
        "code": "SI"
      }, {
        "name": "Spain",
        "code": "ES"
      },  {
        "name": "Sweden",
        "code": "SE"
      }, {
        "name": "Switzerland",
        "code": "CH"
      }, {
        "name": "Turkey",
        "code": "TR"
      },  {
        "name": "United Kingdom",
        "code": "UK"
      }
    ];
  }

  getAll() : CountryDetails[] {
    return this.countryList;
  }
}