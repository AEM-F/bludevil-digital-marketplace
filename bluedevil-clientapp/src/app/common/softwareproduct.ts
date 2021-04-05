import {Product} from "./product";
import {ProductPlatform} from "./productplatform";

export class SoftwareProduct extends Product{

  constructor(name: string, platform: ProductPlatform, description: string, imageUrl: string, systemReq: string, active: boolean, price: number) {
    super(name, platform, description, imageUrl, systemReq, active, price);
    this.setType('softwareproduct');
  }

  getType(): string {
    return "softwareproduct";
  }

}
