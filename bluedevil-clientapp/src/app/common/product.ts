import {ProductPlatform} from "./productplatform";

export abstract class Product {
  public id: number = 0;
  public name: string;
  public productPlatform: ProductPlatform;
  public quantity: number = 0;
  public price: number = 0.00;
  public description: string;
  public imageUrl: string;
  public systemRequirements: string;
  public active: boolean;
  private type: string = '';

  protected constructor(name: string, platform: ProductPlatform, description: string, imageUrl: string, systemReq: string, isActive:boolean) {
    this.name = name;
    this.productPlatform = platform;
    this.description = description;
    this.imageUrl = imageUrl;
    this.systemRequirements = systemReq;
    this.active = isActive;
  }


  public abstract getType(): string

  public setType(type: string): void {
    this.type = type;
  }
}
