import {Product} from "./product";
import {Genre} from "./genre";
import {ProductPlatform} from "./productplatform";

export class Videogame extends Product{
  public releaseDate: string;
  public genres: Genre[];


  constructor(name: string, platform: ProductPlatform, description: string, imageUrl: string, systemReq: string, active: boolean, price: number, releaseDate: string, genres: Genre[]) {
    super(name, platform, description, imageUrl, systemReq, active, price);
    this.releaseDate = releaseDate;
    this.genres = genres;
    this.setType('videogame');
  }

  getType(): string {
    return "videogame";
  }

}
