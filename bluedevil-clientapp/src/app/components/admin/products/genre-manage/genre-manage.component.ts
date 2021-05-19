import {Component, Inject, OnInit} from '@angular/core';
import {GenreService} from '../../../../services/genre.service';
import {Genre} from '../../../../common/genre';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {GenreValidators} from '../../../validators/genre.validators';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {animate, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-genre-manage',
  templateUrl: './genre-manage.component.html',
  styleUrls: ['./genre-manage.component.css'],
  animations: [
    trigger(
      'inOutAnimation',
      [
        transition(
          ':enter',
          [
            style({ opacity: 0 }),
            animate('1s ease-out',
              style({ opacity: 1 }))
          ]
        ),
        transition(
          ':leave',
          [
            style({ opacity: 1 }),
            animate('1s ease-in',
              style({ opacity: 0 }))
          ]
        )
      ]
    )
  ]
})
export class GenreManageComponent implements OnInit {

  previousKeyword = '';
  currentSearchKeyword = '';
  formOption = '';
  isLoadingGenres = false;
  genres: Genre[] = [];
  genreFetchError;
  pageNr = 1;
  pageSize = 5;
  totalElements = 0;
  selectedGenre: Genre = null;
  editGenreForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.maxLength(255),
      GenreValidators.cannotContainSpace
    ])
  });
  genreEditError;
  genreCreateError;
  isUpdateDone = false;
  createGenreForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.maxLength(255),
      GenreValidators.cannotContainSpace
    ])
  });
  isCreateDone: boolean;
  get editFormGenreName(): AbstractControl{
    return this.editGenreForm.get('name');
  }
  get createFormGenreName(): AbstractControl{
    return this.createGenreForm.get('name');
  }

  constructor(private genreService: GenreService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.listGenres();
  }

  onEditFormSubmit(): void{
    this.genreEditError = null;
    const genreName: string = this.editFormGenreName.value;
    this.genreService.checkNameValidity(genreName).subscribe(
      response => {
        if (!response.valid){
          this.genreEditError = 'Update failed, genre name has to be unique';
          setTimeout(() => {
            this.genreEditError = null;
          }, 2000);
        }
        else if (response.valid){
          console.log('Your genre name is valid');
          const genreToUpdate: Genre = new Genre(genreName.toLowerCase());
          this.genreService.updateGenre(genreToUpdate, this.selectedGenre.id).subscribe(
            data => {
              this.selectedGenre = null;
              this.formOption = '';
              this.listGenres();
              this.isUpdateDone = true;
              setTimeout(() => {
                this.isUpdateDone = false;
              }, 2000);
            },
            error2 => {
              this.genreEditError = error2.error.message;
            }
          );
        }
      },
      error => {
        this.genreEditError = error.error.message;
      }
    );
  }

  onCreateSubmit(): void{
   this.genreCreateError = null;
   const genreName: string = this.createFormGenreName.value;
   this.genreService.checkNameValidity(genreName).subscribe(
      response => {
        if (!response.valid){
          this.genreCreateError = 'Creation failed, genre name has to be unique';
          setTimeout(() => {
            this.genreCreateError = null;
          }, 2000);
        }
        else if (response.valid){
          console.log('Your genre name is valid');
          const genreToCreate: Genre = new Genre(genreName.toLowerCase());
          this.genreService.createGenre(genreToCreate).subscribe(
            data => {
              this.selectedGenre = null;
              this.formOption = '';
              this.listGenres();
              this.isCreateDone = true;
              setTimeout(() => {
                this.isCreateDone = false;
              }, 2000);
            },
            error2 => {
              this.genreCreateError = error2.error.message;
            }
          );
        }
      },
      error => {
        this.genreCreateError = error.error.message;
      }
    );
  }

  setFormOption(option: string): void{
    if (this.formOption === option){
      this.formOption = '';
    }
    else if (this.formOption !== option || this.formOption === ''){
      if (option === 'create'){
        this.formOption = option;
        this.editGenreForm.controls.name.patchValue('');
        this.selectedGenre = null;
        this.genreEditError = null;
      }
      else if (option === 'update'){
        if (this.selectedGenre === null){
          this.formOption = '';
          this.genreEditError = 'No genre selected';
          setTimeout(() => {
            this.genreEditError = null;
          }, 2000);
        }
        else{
          this.genreEditError = null;
          this.formOption = option;
        }
      }
    }
  }

  listGenres(): void{
    this.route.queryParams.subscribe((params: Params) => {
      if (params.hasOwnProperty('genreName') && params['genreName'] !== ''){
        this.currentSearchKeyword = params['genreName'];
        this.handleSearchGenres();
      }
      else{
        if (this.currentSearchKeyword !== ''){
          this.pageNr = 1;
          this.currentSearchKeyword = '';
        }
        this.handleListGenres();
      }
    });
  }

  handleListGenres(): void{
    this.isLoadingGenres = true;
    this.genreService.getGenreList('page', this.pageNr, this.pageSize).subscribe(
      this.processGenresFetchData(),
      this.processGenresFetchError()
    );
  }

  handleSearchGenres(): void{
    this.isLoadingGenres = true;
    if (this.previousKeyword !== this.currentSearchKeyword){
      this.pageNr = 1;
    }
    this.previousKeyword = this.currentSearchKeyword;
    this.genreService.searchGenresPaginate(this.pageNr, this.pageSize, this.currentSearchKeyword).subscribe(
      this.processGenresFetchData(),
      this.processGenresFetchError()
    );
  }

  processGenresFetchData(){
    return data => {
      this.isLoadingGenres = false;
      this.genres = data.objectsList;
      this.pageNr = data.pageNumber;
      this.pageSize = data.pageSize;
      this.totalElements = data.totalElements;
    };
  }

  processGenresFetchError(){
    return error => {
      this.isLoadingGenres = false;
      this.pageNr = 1;
      this.isLoadingGenres = false;
      this.genreFetchError = error.error.message;
      this.genres = [];
    };
  }

  checkIfGenreIsSelected(name: string): boolean{
    if (this.selectedGenre === null){
      return false;
    }
    else{
      if (this.selectedGenre.genreName === name){
        return true;
      }
      else {
        return false;
      }
    }
  }

  toggleGenre(genre: Genre): void{
    if ( this.selectedGenre === null){
      this.selectedGenre = genre;
      this.editFormGenreName.patchValue(this.selectedGenre.genreName);
    }
    else if (this.selectedGenre.genreName === genre.genreName){
      this.selectedGenre = null;
    }
    else{
      this.selectedGenre = genre;
      this.editFormGenreName.patchValue(this.selectedGenre.genreName);
    }
  }

  updatePageSize(size: number): void{
    this.pageSize = size;
    this.pageNr = 1;
    this.listGenres();
  }
}
