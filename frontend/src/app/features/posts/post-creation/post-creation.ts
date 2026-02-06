import { Component } from '@angular/core';

@Component({
  selector: 'app-post-creation',
  imports: [],
  templateUrl: './post-creation.html',
  styleUrl: './post-creation.scss',
})
export class PostCreation {}

/*
@Component({
  selector: 'app-post-creation',
  templateUrl: './post-creation.component.html',
  styleUrls: ['./post-creation.component.css']
})
export class PostCreationComponent implements OnInit {
  postForm: FormGroup;

  constructor(
    private fb: FormBuilder, 
    private postService: PostService, 
    private router: Router
  ) {}

  ngOnInit() {
    this.createForm();
  }

  createForm() {
    this.postForm = this.fb.group({
      title: ['', [Validators.required]],
      content: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.postForm.valid) {
      this.postService.createPost(this.postForm.value).subscribe({
        next: (response) => {
          console.log('Post created successfully', response);
          this.router.navigate(['/posts']); // Navigate to the post feed
        },
        error: (err) => {
          console.error('Post creation failed', err);
        }
      });
    }
  }
}

*/
