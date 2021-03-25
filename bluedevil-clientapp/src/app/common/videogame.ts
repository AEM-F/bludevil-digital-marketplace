import {Product} from "./product";
import {Genre} from "./genre";
import {ProductPlatform} from "./productplatform";

export class Videogame extends Product{
  public releaseDate: string;
  public genres: Genre[];


  constructor(name: string, platform: ProductPlatform, description: string, imageUrl: string, systemReq: string, isActive: boolean, releaseDate: string, genres: Genre[]) {
    super(name, platform, description, imageUrl, systemReq, isActive);
    this.releaseDate = releaseDate;
    this.genres = genres;
  }

  getType(): string {
    return "videogame";
  }

}
