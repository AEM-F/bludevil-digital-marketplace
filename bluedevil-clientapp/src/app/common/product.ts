import {ProductPlatform} from "./productplatform";

export abstract class Product {
  public id: number = null;
  public name: string;
  public productPlatform: ProductPlatform;
  public quantity: number = 0;
  public price: number = 0.00;
  public description: string;
  public imageUrl: string;
  public systemRequirements: string;
  public active: boolean;
  public type: string = '';

  protected constructor(name: string, platform: ProductPlatform, description: string, imageUrl: string, systemReq: string, active:boolean, price: number) {
    this.name = name;
    this.productPlatform = platform;
    this.description = description;
    this.imageUrl = imageUrl;
    this.systemRequirements = systemReq;
    this.active = active;
    this.price = price;
  }


  public abstract getType(): string

  public setType(type: string): void {
    this.type = type;
  }
}
