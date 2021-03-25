import {Product} from "./product";
import {ProductPlatform} from "./productplatform";

export class SoftwareProduct extends Product{

  constructor(name: string, platform: ProductPlatform, description: string, imageUrl: string, systemReq: string, isActive: boolean) {
    super(name, platform, description, imageUrl, systemReq, isActive);
  }

  getType(): string {
    return "softwareproduct";
  }

}
